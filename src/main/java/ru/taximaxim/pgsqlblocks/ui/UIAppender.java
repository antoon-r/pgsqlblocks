package ru.taximaxim.pgsqlblocks.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class UIAppender extends WriterAppender{

    private Composite parent;
    private StyledText text;
    private Display display;

    public UIAppender(Composite parent) {
        this.parent = parent;
        this.display = parent.getDisplay();
        createControl();
    }

    private void createControl() {
        text = new StyledText(parent, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        text.layout(true);
        parent.layout(true, true);
    }

    public void append(LoggingEvent event) {
        if(display == null || display.isDisposed() || text == null)
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date time = new Date(event.getTimeStamp());
        String dateTime = sdf.format(time);
        String excMessage = "";
        try{
            excMessage = (String)event.getMessage();
        } catch(Exception e) {
            e.printStackTrace();
        }
        final String logMessage = String.format("[%s] %s\n",dateTime,excMessage);
        parent.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                text.append(logMessage);
            }
        });
    }
}
