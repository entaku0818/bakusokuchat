package com.entaku.bakusokuchat

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firestore.v1.Document
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel(application:Application):AndroidViewModel(application) {
   val reference by lazy { FirebaseFirestore.getInstance() }
   val data= ArrayList<Message>()
    val dataChanged:MutableLiveData<Unit> = MutableLiveData()
    val dataInserted:MutableLiveData<Int> = MutableLiveData()
    val messageDraft:MutableLiveData<String> =MutableLiveData()
    val canSendText=MediatorLiveData<Boolean>().also {
        result->
        result.addSource(messageDraft){result.value=messageDraft.value!!.isNotEmpty()}
    }


    fun initializeViewModel(){
        initializeMessage()
        listeningMessage()
    }
    private fun listeningMessage(){
        reference.collection("version/1/talk").orderBy("createdAt").addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException!=null)
            {
                Log.w("Warn",firebaseFirestoreException)
                return@addSnapshotListener
            }
            querySnapshot?.documentChanges?.map {
                when(it.type){
                    DocumentChange.Type.ADDED->{data.add(docToMessage(it.document))
                                                      dataInserted.value=data.size+1
                    }
                    DocumentChange.Type.MODIFIED->{}
                    DocumentChange.Type.REMOVED->{data.remove(docToMessage(it.document))
                    }
                }
            }

        }
    }
    private fun docToMessage(doc:QueryDocumentSnapshot):Message{
        return    Message(text= doc.getString("text")?:"" ,createdAt = doc.getDate("createdAt")!!
            ,profileUrl = doc.getString("thumbnailURL")?:""
        )
    }
   private fun initializeMessage(){
       Log.d("error", "initialized")
      reference.collection("/version/1/talk").orderBy("createdAt").get()
         .addOnCompleteListener {
             Log.w("",it.toString())
            if (it.isSuccessful){

           val results=it.result?.map { result->
               Message(text= result.getString("text")?:"" ,createdAt = result.getDate("createdAt")!!
               ,profileUrl = result.getString("thumbnailURL")?:""
               )

            }!!.toTypedArray()
                data.addAll(results)
                dataChanged.value=Unit
         }
         else{
                Log.w("error","Error connection to DB")
            }
         }
          .addOnSuccessListener {
              Log.w("error",it.isEmpty.toString()) }
   }
    fun sendMessage(){
        val message=HashMap<String,Any>()
        message["text"] = messageDraft.value?:""
        message["createdAt"] = Date()
        message["thumbnailURL"]="https://i2.wp.com/www.neko-cats.net/wp-content/uploads/2016/08/image-22.jpeg"
        message["updatedAt"]=Date()
        canSendText.value?.let{
            if (it){
                reference.collection("/version/1/talk")
                    .add(message)
                    .addOnSuccessListener {
                        messageDraft.value=""
                        Toast.makeText(getApplication(),"成功しました",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {ex->
                        Toast.makeText(getApplication(),ex.printStackTrace().toString(),Toast.LENGTH_SHORT).show()
                    }
            }
            else{

            }

        }



    }

}