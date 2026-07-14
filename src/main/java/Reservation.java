public class Reservation {
    private int id;
    private int deviceId;
    private String userName;
    private String date;
    private String timeSlot;

    public Reservation(int id, int deviceId, String userName, String date, String timeSlot) {
        this.id = id;
        this.deviceId = deviceId;
        this.userName = userName;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public int getId() {
        return id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void printInfo() {
        System.out.println("预约编号：" + id);
        System.out.println("设备编号：" + deviceId);
        System.out.println("预约人：" + userName);
        System.out.println("预约日期：" + date);
        System.out.println("预约时间：" + timeSlot);
        System.out.println("--------------------");
    }
}