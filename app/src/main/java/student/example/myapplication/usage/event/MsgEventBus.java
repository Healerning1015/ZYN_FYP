package student.example.myapplication.usage.event;

import org.greenrobot.eventbus.EventBus;

/**
 * 消息传递工具（暂未使用）
 */

public class MsgEventBus {

    private static EventBus mEventBus;

    public static EventBus getInstance()
    {
        if (mEventBus == null)
        {
            mEventBus = new EventBus();
        }
        return mEventBus;
    }

}
