package student.example.myapplication.usage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is history usage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}