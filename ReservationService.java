import java.util.ArrayList;

public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public Reservation addReservation(ArrayList<Device> devices, ArrayList<Reservation> reservations,
                                      int deviceId, String userName, String date, String timeSlot) throws Exception {
        Device selectedDevice = findDeviceById(devices, deviceId);

        if (selectedDevice == null) {
            System.out.println("设备不存在，预约失败");
            return null;
        }

        if (!selectedDevice.getStatus().equals("可预约")) {
            System.out.println("该设备当前状态为：" + selectedDevice.getStatus());
            System.out.println("设备不可预约，预约失败");
            return null;
        }

        if (!isValidTimeSlot(timeSlot)) {
            System.out.println("时间段格式错误，预约失败");
            return null;
        }

        if (hasReservationConflict(reservations, deviceId, date, timeSlot)) {
            System.out.println("该设备在这个日期和时间段已有预约冲突，预约失败");
            return null;
        }

        Reservation reservation = new Reservation(0, deviceId, userName, date, timeSlot);
        int newId = reservationDao.insert(reservation);

        return new Reservation(newId, deviceId, userName, date, timeSlot);
    }

    public boolean deleteReservation(ArrayList<Reservation> reservations, int reservationId) throws Exception {
        Reservation targetReservation = null;

        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                targetReservation = reservation;
                break;
            }
        }

        if (targetReservation == null) {
            System.out.println("预约记录不存在，删除失败");
            return false;
        }

        reservationDao.deleteById(reservationId);
        reservations.remove(targetReservation);

        return true;
    }

    public Device findDeviceById(ArrayList<Device> devices, int deviceId) {
        for (Device device : devices) {
            if (device.getId() == deviceId) {
                return device;
            }
        }

        return null;
    }

    public boolean hasReservationConflict(ArrayList<Reservation> reservations, int deviceId, String date, String newTimeSlot) {
        for (Reservation reservation : reservations) {
            if (reservation.getDeviceId() == deviceId && reservation.getDate().equals(date)) {
                if (isTimeOverlap(reservation.getTimeSlot(), newTimeSlot)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isTimeOverlap(String existingTimeSlot, String newTimeSlot) {
        String[] existingParts = existingTimeSlot.split("-");
        String[] newParts = newTimeSlot.split("-");

        int existingStart = convertTimeToMinutes(existingParts[0]);
        int existingEnd = convertTimeToMinutes(existingParts[1]);
        int newStart = convertTimeToMinutes(newParts[0]);
        int newEnd = convertTimeToMinutes(newParts[1]);

        return newStart < existingEnd && existingStart < newEnd;
    }

    public boolean isValidTimeSlot(String timeSlot) {
        String[] parts = timeSlot.split("-");

        if (parts.length != 2) {
            return false;
        }

        try {
            int start = convertTimeToMinutes(parts[0]);
            int end = convertTimeToMinutes(parts[1]);

            return start < end;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidTime(String time) {
        String[] parts = time.split(":");

        if (parts.length != 2) {
            return false;
        }

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
    }

    public int convertTimeToMinutes(String time) {
        if (!isValidTime(time)) {
            throw new IllegalArgumentException("Invalid time format");
        }

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return hour * 60 + minute;
    }
}