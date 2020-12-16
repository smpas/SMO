import java.util.ArrayList;

public class BufferDispatcher {
    private final Buffer buffer;
    private final ArrayList<Source> sources;
    private final SelectionDispatcher SelectionDispatcher;

    private Source currentSource;
    private Request currentRequest;

    public BufferDispatcher(Buffer buffer, ArrayList<Source> sources, SelectionDispatcher SelectionDispatcher) {
        this.buffer = buffer;
        this.sources = sources;
        this.SelectionDispatcher = SelectionDispatcher;
    }

    public void initSources() {
        for (Source s : sources) {
            s.generate();
        }
    }

    public void generate() {
        currentSource.generate();
    }

    public void setCurrentSource() {
        Source source = sources.get(0);
        for (Source s : sources) {
            if (s.getGenerationTime() < source.getGenerationTime()) {
                source = s;
            }
        }

        currentSource = source;
    }

    public void putRequest() {
        if (buffer.isEmpty() && SelectionDispatcher.hasFreeDevice(currentRequest.getGenerationTime())) {
            SelectionDispatcher.getRequestFromSource(currentRequest);

            SelectionDispatcher.putInDevice();
        } else {
            if (buffer.hasFreeSpaces()) {
                buffer.putRequest(currentRequest);
            } else {
                buffer.refuseRequest(currentRequest);
            }
        }
    }

    public Source getCurrentSource() {
        return currentSource;
    }

    public void getRequest() {
        currentRequest = currentSource.getRequest();
    }

    public ArrayList<Source> getSources() {
        return sources;
    }
}
