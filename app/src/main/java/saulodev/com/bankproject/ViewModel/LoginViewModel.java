package saulodev.com.bankproject.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<String> mutableliveData_cpf= new MutableLiveData<>();
    public LiveData<String> LiveData_cpf = mutableliveData_cpf;

    private MutableLiveData<String> mutableliveData_senha= new MutableLiveData<>();
    public LiveData<String> LiveData_senha = mutableliveData_senha;
}
