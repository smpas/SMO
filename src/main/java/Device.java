public class Device {
    private final int deviceNum;
    private final double a;
    private final double b;
    private double startTime = 0;
    private double releaseTime = 0;
    private double downtime = 0;
    private int pack;
    private int requestNum;

    public Device(int deviceNum, double a, double b) {
        this.deviceNum = deviceNum;
        this.a = a;
        this.b = b;
    }

    public Request serve(Request request) {
        if (request.getGenerationTime() > startTime) {
            downtime += request.getGenerationTime() - startTime;
            startTime = request.getGenerationTime();
        }
        else {
            request.setWaitTime(startTime - request.getGenerationTime());
        }
        pack = request.getSourceNumber();
        requestNum = request.getRequestNumber();
        double serveTime = (b - a) * Math.random() + a;
        request.setServeTime(serveTime);
        releaseTime = startTime + serveTime;
        startTime = releaseTime;

        return request;
    }

    public double getReleaseTime() {
        return releaseTime;
    }

    public double getDowntime() {
        return downtime;
    }

    public int getDeviceNum() {
        return deviceNum;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public int getPack() {
        return pack;
    }
}
