import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private int requestNumber = 0;
    private int N;
    private BufferDispatcher BufferDispatcher;
    private SelectionDispatcher SelectionDispatcher;
    private Buffer buffer;

    private Scanner input;

    public void init() {
        input = new Scanner(System.in);

        System.out.println("Введите количество источников:");
        int sourceNum = input.nextInt();

        System.out.println("Введите интенсивность источников:");
        double flowIntensity = input.nextDouble();

        System.out.println("Введите кол-во приборов:");
        int deviceNum = input.nextInt();

        System.out.println("Введите a:");
        double a = input.nextDouble();

        System.out.println("Введите b:");
        double b = input.nextDouble();

        System.out.println("Введите размер буфера:");
        int size = input.nextInt();

        System.out.println("Введите количество заявок:");
        N = input.nextInt();

        ArrayList<Source> sources = new ArrayList<>(sourceNum);
        for (int i = 0; i < sourceNum; ++i) {
            sources.add(new Source(i, flowIntensity));
        }

        ArrayList<Device> devices = new ArrayList<>(deviceNum);
        for (int i = 0; i < deviceNum; ++i) {
            devices.add(new Device(i, a, b));
        }

        buffer = new Buffer(size, sourceNum);
        SelectionDispatcher = new SelectionDispatcher(buffer, devices, sourceNum);
        BufferDispatcher = new BufferDispatcher(buffer, sources, SelectionDispatcher);

        BufferDispatcher.initSources();
    }

    public void run(int mode) {
        Stat stat = new Stat(BufferDispatcher, SelectionDispatcher, buffer);
        while (requestNumber < N ) {
            BufferDispatcher.setCurrentSource();
            SelectionDispatcher.setCurrentDevice();

            if (!buffer.isEmpty() && (SelectionDispatcher.getCurrentDevice().getReleaseTime() <
                                        BufferDispatcher.getCurrentSource().getGenerationTime())) {
                SelectionDispatcher.getRequestFromBuffer();
                SelectionDispatcher.putInDevice();
            } else {
                BufferDispatcher.getRequest();
                BufferDispatcher.putRequest();
                BufferDispatcher.generate();
                ++requestNumber;
            }

            if (mode == 2) {
                stat.getSourcesState();
                stat.getBufferState();
                stat.getDevicesState();
                if (input.nextInt() != 1) {
                    break;
                }
            }
        }
        while (!buffer.isEmpty()) {
            SelectionDispatcher.setCurrentDevice();
            SelectionDispatcher.getRequestFromBuffer();
            SelectionDispatcher.putInDevice();
            if (mode == 2) {
                stat.getSourcesState();
                stat.getBufferState();
                stat.getDevicesState();
                if (input.nextInt() != 1) {
                    break;
                }
            }
        }

        if (mode == 1) {
            stat.getSourceTable();
            stat.getDeviceTable();
        }
    }
}
