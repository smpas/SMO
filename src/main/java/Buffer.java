import java.util.ArrayList;

public class Buffer {
    private final int size;
    private int requestNum;
    private final ArrayList<Request> queue;
    private final int[] refusedRequests;
    private int pointer;
    private int priority;

    public Buffer(int size, int sourceNum) {
        this.size = size;
        this.queue = new ArrayList<>(size);
        this.requestNum = 0;
        this.refusedRequests = new int[sourceNum];
        this.pointer = 0;
        for (int i = 0; i < size; i++) {
            this.queue.add(i, null);
        }
    }

    public int NextPointer(int pointer) {
        if ((pointer + 1) == size) return 0;
        else return pointer + 1;
    }

    public int PrevPointer(int pointer) {
        if ((pointer - 1) < 0) return size - 1;
        else return pointer - 1;
    }

    public boolean isEmpty() {
        int null_counter = 0;
        for (int i = 0; i < size; i++) {
            if (queue.get(i) == null) null_counter++;
        }
        if (null_counter < size) return false;
        else return true;
    }

    public boolean hasFreeSpaces() {
        return requestNum < size;
    }

    public void putRequest(Request request) {
        int place = -1;
        int req = request.getRequestNumber();
        for (int i = 0; i < size; i++) {
            if (queue.get(pointer) == null) {
                queue.set(pointer, request);
                ++requestNum;
                place = pointer;
                pointer = NextPointer(pointer);
                System.out.println("Заявка номер " + req + " от " + request.getSourceNumber() + " источника встала на " + place + " место в буфере");
                return;
            }
            pointer = NextPointer(pointer);
        }
    }

    public void refuseRequest(Request request) {
        int newestRequestIndex = PrevPointer(pointer);
        Request refusedRequest = queue.get(newestRequestIndex);
        ++refusedRequests[refusedRequest.getSourceNumber()];
        queue.set(newestRequestIndex, request);
        int req = request.getRequestNumber();
        System.out.println("Заявке номер " + req + " от " + request.getSourceNumber() + " источника отказано в обслуживании");
    }

    Request getFirstRequest() {
        int firstNumber = 0;
        double firstRequestTime;
        Request firstRequest;
        updatePriority();

        while (true) {
            try {
                if (queue.get(firstNumber).getSourceNumber() == priority) {
                    firstRequest = queue.get(firstNumber);
                    firstRequestTime = firstRequest.getGenerationTime();
                    break;
                }
                firstNumber++;
            } catch (NullPointerException exc) {
                firstNumber++;
            }
        }

        for (Request req : queue) {
            try {
                if (req.getGenerationTime() < firstRequestTime & req.getSourceNumber() == priority) {
                    firstNumber = queue.indexOf(req);
                    firstRequest = req;
                    firstRequestTime = firstRequest.getGenerationTime();
                }
            } catch (NullPointerException exc) {
            }
        }

        queue.set(firstNumber, null);
        --requestNum;
        return firstRequest;
    }

    public void updatePriority() {
        int min = 1000000;
        for (int i = 0; i < size; i++) {
            if (queue.get(i) != null) {
                if (queue.get(i).getSourceNumber() == priority) return;
                if (queue.get(i).getSourceNumber() < min) {
                    min = queue.get(i).getSourceNumber();
                }
            }
        }
        setPriority(min);
        return;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPointer() {
        return pointer;
    }

    public int getPriority() {
        return priority;
    }

    public int[] getRefusedRequests() {
        return refusedRequests;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Request> getQueue() {
        return queue;
    }
}
