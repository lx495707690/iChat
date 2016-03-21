package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "me.itangqi.greendao");
//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
//      Schema schema = new Schema(1, "me.itangqi.bean");
//      schema.setDefaultJavaPackageDao("me.itangqi.dao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        addTables(schema);

        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
        new DaoGenerator().generateAll(schema, "D:\\Workspaces\\ichat_android_new\\chat_main\\src\\main\\java-gen");
    }

    /**
     * @param schema
     */
    private static void addTables(Schema schema) {
//        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        /* entities */
        addChat(schema);
        addMessage(schema);
        addFriend(schema);
    }

    /**
     * Create chat Properties
     *
     * @return DBFriend entity
     */
    private static void addFriend(Schema schema) {
        Entity chat = schema.addEntity("DBFriend");
        chat.addIdProperty().primaryKey().autoincrement();
        chat.addStringProperty("my_userId").notNull();
        chat.addStringProperty("friend_userId").notNull();
        chat.addStringProperty("name").notNull();
        chat.addStringProperty("avatar");
        chat.addStringProperty("phone");
        chat.addStringProperty("address");
    }

    /**
     * Create chat Properties
     *
     * @return DBChat entity
     */
    private static void addChat(Schema schema) {
        Entity chat = schema.addEntity("DBChat");
        chat.addIdProperty().primaryKey().autoincrement();
        chat.addStringProperty("my_userId").notNull();
        chat.addStringProperty("channalId").notNull();
        chat.addStringProperty("friend_userId").notNull();
        chat.addStringProperty("name").notNull();
        chat.addStringProperty("message").notNull();
        chat.addStringProperty("date").notNull();
        chat.addStringProperty("receive_message_date");
        chat.addStringProperty("image").notNull();
        chat.addStringProperty("unread_num").notNull();
    }

    /**
     * Create message Properties
     *
     * @return DBPhoneNumber entity
     */
    private static void addMessage(Schema schema) {
        Entity message = schema.addEntity("DBMessage");
        message.addIdProperty().primaryKey().autoincrement();
        message.addStringProperty("my_userId").notNull();
        message.addStringProperty("message").notNull();
        message.addStringProperty("date").notNull();
        message.addStringProperty("fromId").notNull();
        message.addStringProperty("toId").notNull();
        message.addStringProperty("channelId").notNull();
        message.addStringProperty("image").notNull();
        message.addStringProperty("from_name").notNull();
        message.addStringProperty("channal_name").notNull();
        message.addBooleanProperty("from_me").notNull();
        message.addBooleanProperty("is_sended").notNull();
    }
}
