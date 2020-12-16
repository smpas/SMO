import java.util.ArrayList;

public class SelectionDispatcher {
    private final Buffer buffer;
    private final ArrayList<Device> devices;

    private Device currentDevice;
    private Request currentRequest;

    private final double[] waitTime;
    private final double[] serveTime;

    public SelectionDispatcher(Buffer buffer, ArrayList<Device> devices, int sourceNum) {
        this.buffer = buffer;
        this.devices = devices;
        this.waitTime = new double[sourceNum];
        this.serveTime = new double[sourceNum];
    }

    public boolean hasFreeDevice(double currentTime) {
        for (Device device : devices) {
            if (device.getReleaseTime() < currentTime) {
                return true;
            }
        }
        return false;
    }

    public void setCurrentDevice() {
        currentDevice = devices.get(0);
        for (Device device : devices) {
            if (device.getReleaseTime() < currentDevice.getReleaseTime()) {
                currentDevice = device;
            }
        }
    }

    public void putInDevice() {
        int req = currentRequest.getRequestNumber();
        System.out.println("Заявка номер " + req + " от " + currentRequest.getSourceNumber() +
                " источника встала на обслуживание в " + currentDevice.getDeviceNum() + " прибор");
        Request request = currentDevice.serve(currentRequest);
        waitTime[request.getSourceNumber()] += request.getWaitTime();
        serveTime[request.getSourceNumber()] += request.getServeTime();
    }

    public double getLastReleaseTime() {
        double time = devices.get(0).getReleaseTime();
        for (Device device : devices) {
            if (device.getReleaseTime() > time) {
                time = device.getReleaseTime();
            }
        }

        return time;
    }

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void getRequestFromSource(Request request) {
        currentRequest = request;
    }

    public void getRequestFromBuffer() {
        currentRequest = buffer.getFirstRequest();
    }

    public double[] getWaitTime() {
        return waitTime;
    }

    public double[] getServeTime() {
        return serveTime;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
