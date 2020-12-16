public class Request {
    private final double generationTime;
    private double waitTime = 0;
    private double serveTime;
    private final int requestNumber;
    private final int sourceNumber;

    public Request(double generationTime, int requestNumber, int sourceNumber) {
        this.generationTime = generationTime;
        this.requestNumber = requestNumber;
        this.sourceNumber = sourceNumber;
    }

    public double getGenerationTime() {
        return generationTime;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public int getSourceNumber() {
        return sourceNumber;
    }

    public double getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public double getServeTime() {
        return serveTime;
    }

    public void setServeTime(double serveTime) {
        this.serveTime = serveTime;
    }
}
