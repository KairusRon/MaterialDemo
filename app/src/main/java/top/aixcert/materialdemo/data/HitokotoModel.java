package top.aixcert.materialdemo.data;

/**
 * {
 *     "id": 4956,
 *     "uuid": "78a352f7-0d79-4663-bac0-fd07ca246e7f",
 *     "hitokoto": "连我自己都轻视自己的话，谁来夸奖我啊，只有我了，舍我其谁。",
 *     "type": "f",
 *     "from": "笹木咲",
 *     "from_who": null,
 *     "creator": "ouuan",
 *     "creator_uid": 2676,
 *     "reviewer": 4756,
 *     "commit_from": "web",
 *     "created_at": "1576373899",
 *     "length": 29
 * }
 */
public class HitokotoModel {
    private int id;
    private String uuid;
    private String hitokoto;
    private String type;
    private String from;
    private String from_who;
    private String creator;
    private int creator_uid;
    private int reviewer;
    private String commit_from;
    private String created_at;
    private int length;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHitokoto() {
        return hitokoto;
    }

    public void setHitokoto(String hitokoto) {
        this.hitokoto = hitokoto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom_who() {
        return from_who;
    }

    public void setFrom_who(String from_who) {
        this.from_who = from_who;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getCreator_uid() {
        return creator_uid;
    }

    public void setCreator_uid(int creator_uid) {
        this.creator_uid = creator_uid;
    }

    public int getReviewer() {
        return reviewer;
    }

    public void setReviewer(int reviewer) {
        this.reviewer = reviewer;
    }

    public String getCommit_from() {
        return commit_from;
    }

    public void setCommit_from(String commit_from) {
        this.commit_from = commit_from;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
