package ru.taximaxim.pgsqlblocks.process;

import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import ru.taximaxim.pgsqlblocks.SortColumn;
import ru.taximaxim.pgsqlblocks.TreeLabelProvider;

public class ProcessTreeLabelProvider extends TreeLabelProvider implements ITableLabelProvider {

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        Process process = (Process) element;
        switch (columnIndex) {
            case 0: return getImage(process.getStatus().getStatusImage().getImageAddr());
            default: return null;
        }
    }

    @Override
    public String getColumnText(Object element, int columnIndex) {
        Process process = (Process) element;
        switch (SortColumn.values()[columnIndex]) {
            case PID: return String.valueOf(process.getPid());
            case BLOCKED_COUNT: return String.valueOf(process.getChildren().size());
            case APPLICATION_NAME: return process.getCaller().getApplicationName();
            case DATNAME: return process.getCaller().getDatname();
            case USENAME: return process.getCaller().getUsername();
            case CLIENT: return process.getCaller().getClient();
            case BACKEND_START: return process.getQuery().getBackendStart();
            case QUERY_START: return process.getQuery().getQueryStart();
            case XACT_START: return process.getQuery().getXactStart();
            case STATE: return process.getState();
            case STATE_CHANGE: return process.getStateChange();
            case BLOCKED: return process.getBlocks().stream()
                                .map(b -> String.valueOf(b.getBlockingPid()))
                                .collect(Collectors.joining(","));
            case LOCKTYPE: return process.getBlocks().stream()
                                .map(Block::getLocktype)
                                .distinct()
                                .collect(Collectors.joining(","));
            case RELATION: return process.getBlocks().stream()
                                .map(Block::getRelation)
                                .filter(r -> r != null && !r.isEmpty())
                                .distinct()
                                .collect(Collectors.joining(","));
            case QUERY: return process.getQuery().getQueryString();
            case SLOWQUERY: return String.valueOf(process.getQuery().isSlowQuery());
            default: return null;
        }
    }
    
    private Image getImage(String path) {
        return imagesMap.computeIfAbsent(path, k ->
                new Image(null, getClass().getClassLoader().getResourceAsStream(path)));
    }
}
