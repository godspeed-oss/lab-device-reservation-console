public class Device {
    private int id;
    private String name;
    private String type;
    private String status;

    public Device(int id, String name, String type, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void printInfo() {
        System.out.println("设备编号：" + id);
        System.out.println("设备名称：" + name);
        System.out.println("设备类型：" + type);
        System.out.println("设备状态：" + status);
        System.out.println("--------------------");
    }
}