import java.util.Map;

public class Guest extends Users {


    public Guest(int user_id, String name, String phone) {
        super(user_id, name, phone);
    }

    @Override
    public String toString() {
        return "Guest{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
