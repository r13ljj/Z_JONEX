package reactor;

/**
 * Created by xubai on 2018/10/10 下午4:52.
 */
public class Input {

    private String name;

    public Input(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Input{" +
                "name='" + name + '\'' +
                '}';
    }
}
