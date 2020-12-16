import dnl.utils.text.table.TextTable;

public class Stat {
    private final BufferDispatcher BufferDispatcher;
    private final SelectionDispatcher SelectionDispatcher;
    private final Buffer buffer;

    public Stat(BufferDispatcher BufferDispatcher, SelectionDispatcher SelectionDispatcher, Buffer buffer) {
        this.BufferDispatcher = BufferDispatcher;
        this.SelectionDispatcher = SelectionDispatcher;
        this.buffer = buffer;
    }

    public void getSourceTable() {
        String[] columnNames = {"№", "Число заявок", "Вероятность отказа",
                                "Среднее время пребывания", "Среднее время ожидания", "Среднее время обслуживания"};

        int[] sourceNum = new int[BufferDispatcher.getSources().size()];
        int[] requestNum = new int[BufferDispatcher.getSources().size()];
        Double[] failureProbability = new Double[BufferDispatcher.getSources().size()];
        Double[] averageTimeInSystem = new Double[BufferDispatcher.getSources().size()];
        Double[] averageWaitTime = new Double[BufferDispatcher.getSources().size()];
        Double[] averageServeTime = new Double[BufferDispatcher.getSources().size()];

        Object[][] data = new Object[BufferDispatcher.getSources().size()][];

        for (int i = 0; i < BufferDispatcher.getSources().size(); ++i) {
            sourceNum[i] = BufferDispatcher.getSources().get(i).getSourceNum();
            requestNum[i] = (BufferDispatcher.getSources().get(i).getRequestNum() - 1);
            failureProbability[i] = Double.valueOf(buffer.getRefusedRequests()[i]) / Double.valueOf(requestNum[i]);
            averageWaitTime[i] = SelectionDispatcher.getWaitTime()[i] / requestNum[i];
            averageServeTime[i] = SelectionDispatcher.getServeTime()[i] / requestNum[i];
            averageTimeInSystem[i] = averageWaitTime[i] + averageServeTime[i];

            Object[] string = {sourceNum[i], requestNum[i], failureProbability[i],
                    averageTimeInSystem[i], averageWaitTime[i], averageServeTime[i]};
            data[i] = string;
        }
        System.out.println("\nСтатистика источников:");
        TextTable sourceTable = new TextTable(columnNames, data);
        sourceTable.printTable();
    }

    public void getDeviceTable() {
        String[] columnNames = {"№", "Коэф. использования"};

        int[] deviceNum = new int[SelectionDispatcher.getDevices().size()];
        Double[] utilization = new Double[SelectionDispatcher.getDevices().size()];

        Object[][] data = new Object[SelectionDispatcher.getDevices().size()][];

        for (int i = 0; i < SelectionDispatcher.getDevices().size(); ++i) {
            deviceNum[i] = SelectionDispatcher.getDevices().get(i).getDeviceNum();
            utilization[i] = (SelectionDispatcher.getDevices().get(i).getReleaseTime() -
                                SelectionDispatcher.getDevices().get(i).getDowntime()) /
                                SelectionDispatcher.getLastReleaseTime();

            Object[] string = {deviceNum[i], utilization[i]};
            data[i] = string;
        }
        System.out.println("\nСтатистика приборов:");
        TextTable deviceTable = new TextTable(columnNames, data);
        deviceTable.printTable();
    }

    public void getBufferState() {
        String[] columnNames = {"Указатель", "Позиция", "Время генерации", "№ источника", "№ заявки"};

        String[] pointer = new String[buffer.getSize()];
        int[] position = new int[buffer.getSize()];
        Double[] genTime = new Double[buffer.getSize()];
        int[] sourceNum = new int[buffer.getSize()];
        int[] requestNum = new int[buffer.getSize()];

        Object[][] data = new Object[buffer.getSize()][];

        for (int i = 0; i < buffer.getQueue().size(); ++i) {
            try {
                if (buffer.getPointer() == i) pointer[i] = "---->";
                else pointer[i] = "";
                position[i] = i;
                genTime[i] = buffer.getQueue().get(i).getGenerationTime();
                sourceNum[i] = buffer.getQueue().get(i).getSourceNumber();
                requestNum[i] = buffer.getQueue().get(i).getRequestNumber();
                Object[] string = {pointer[i], position[i], genTime[i], sourceNum[i], requestNum[i]};
                data[i] = string;
            } catch ( NullPointerException e ) {
                if (buffer.getPointer() == i) pointer[i] = "---->";
                else pointer[i] = "";
                position[i] = i;
                Object[] string = {pointer[i], position[i], null, null, null};
                data[i] = string;
                continue; }
        }

        System.out.println("\nСостояние буфера:");
        TextTable bufferState = new TextTable(columnNames, data);
        bufferState.printTable();
    }

    public void getSourcesState() {
        String[] columnNames = {"Приоритет", "№", "Время генерации", "Кол-во заявок", "Заявки с отказом"};

        String[] priority = new String[BufferDispatcher.getSources().size()];
        int[] sourceNum = new int[BufferDispatcher.getSources().size()];
        Double[] genTime = new Double[BufferDispatcher.getSources().size()];
        int[] reqNum = new int[BufferDispatcher.getSources().size()];
        int[] rejReqNum = new int[BufferDispatcher.getSources().size()];

        Object[][] data = new Object[BufferDispatcher.getSources().size()][];

        for (int i = 0; i < BufferDispatcher.getSources().size(); ++i) {
            if (buffer.getPriority() == i) priority[i] = "---->";
            else priority[i] = "";
            sourceNum[i] = BufferDispatcher.getSources().get(i).getSourceNum();
            genTime[i] = BufferDispatcher.getSources().get(i).getGenerationTime();
            reqNum[i] = BufferDispatcher.getSources().get(i).getRequestNum();
            rejReqNum[i] = buffer.getRefusedRequests()[i];

            Object[] string = {priority[i], sourceNum[i], genTime[i], reqNum[i], rejReqNum[i]};
            data[i] = string;
        }
        System.out.println("\nСостояние источников:");
        TextTable sourcesState = new TextTable(columnNames, data);
        sourcesState.printTable();
    }

    public void getDevicesState() {
        String[] columnNames = {"№", "Время освобождения", "Время простоя", "№ пакета", "№ заявки"};

        int[] deviceNum = new int[SelectionDispatcher.getDevices().size()];
        Double[] releaseTime = new Double[SelectionDispatcher.getDevices().size()];
        Double[] downtime = new Double[SelectionDispatcher.getDevices().size()];
        int[] pack = new int[SelectionDispatcher.getDevices().size()];
        int[] requestNum = new int[SelectionDispatcher.getDevices().size()];

        Object[][] data = new Object[SelectionDispatcher.getDevices().size()][];

        for (int i = 0; i < SelectionDispatcher.getDevices().size(); ++i) {
            deviceNum[i] = SelectionDispatcher.getDevices().get(i).getDeviceNum();
            releaseTime[i] = SelectionDispatcher.getDevices().get(i).getReleaseTime();
            downtime[i] = SelectionDispatcher.getDevices().get(i).getDowntime();
            pack[i] = SelectionDispatcher.getDevices().get(i).getPack();
            requestNum[i] = SelectionDispatcher.getDevices().get(i).getRequestNum();

            Object[] string = {deviceNum[i], releaseTime[i], downtime[i], pack[i], requestNum[i]};
            data[i] = string;
        }
        System.out.println("\nСостояние приборов:");
        TextTable devicesState = new TextTable(columnNames, data);
        devicesState.printTable();
    }
}
