package data.source;

import android.database.Cursor;
import java.util.List;

import beans.ChatBean;
import beans.CommentBean;
import beans.ContactBean;
import beans.MessageBean;
import beans.MomentBean;
import beans.UserInfoBean;

/**
 * Created by lbf on 2016/7/30.
 */
public interface ChatsDataSource {
//    保存聊天列表
    void saveChatsList(List<ChatBean> chatBeanList, int onTopNumber);
//    删除公众号
    void deleteAccount(int accountId);
//    添加公众号
    void addAccount(ContactBean bean);
//    删除联系人
    void deleteContact(int contactId);
//    添加联系人
    void addContact(ContactBean bean);
//    获取聊天列表
    List<ChatBean> getChatsList();
//    获取联系人列表
    List<ContactBean> getContactsList();
//    获取公众号列表
    List<ContactBean> getAccountsList();
//    获取联系人列表cursor
    Cursor getContactsCursor();

    Cursor getAccountsCursor();


//    获取联系人信息
    ContactBean getContactInfo(int contactId);
//    获取消息列表
    List<MessageBean> getMessageList(int contactId, int page);

    void saveMessageList(List<MessageBean> beanList, int contactId);

    List<MomentBean> getMomentList(int page);

    ContactBean[] getFavors(int id);

    CommentBean[] getComments(int id);

//    获取用户信息
    UserInfoBean getUserInfo();

}
