/*
 * ========================LICENSE_START=================================
 * pgSqlBlocks
 * *
 * Copyright (C) 2017 "Technology" LLC
 * *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package ru.taximaxim.pgsqlblocks.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import ru.taximaxim.pgsqlblocks.SortColumn;
import ru.taximaxim.pgsqlblocks.utils.Settings;

import java.util.*;

public class SettingsDlg extends Dialog {

    private final Settings settings;
    private final ResourceBundle resources;
    private Spinner updatePeriod;
    private Button showIdleButton;
    private Button showToolTip;
    private Button showBackendPidButton;
    private Button confirmRequiredButton;
    private Button confirmExitButton;
    private Combo languageCombo;

    private Set<SortColumn> enabledColumns = new HashSet<>();

    public SettingsDlg(Shell shell, Settings settings) {
        super(shell);
        this.settings = settings;
        this.resources = settings.getResourceBundle();
        enabledColumns.addAll(settings.getColumnsList());
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);

        GridLayout layout = new GridLayout(2, false);
        layout.marginRight = 5;
        layout.marginLeft = 10;
        container.setLayout(layout);

        populateGeneralGroup(container);
        populateProcessGroup(container);
        populateNotificationGroup(container);
        populateColumnGroup(container);
        return container;
    }

    private void populateGeneralGroup(Composite container) {
        Group generalGroup = new Group(container, SWT.SHADOW_IN);
        generalGroup.setText(resources.getString("general"));
        generalGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        generalGroup.setLayout(new GridLayout(2, false));

        Label selectLocale = new Label(generalGroup, SWT.HORIZONTAL);
        selectLocale.setText(resources.getString("select_ui_language"));
        selectLocale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        languageCombo = new Combo (generalGroup, SWT.READ_ONLY);
        languageCombo.setItems(Settings.SUPPORTED_LANGUAGES);
        languageCombo.select(languageCombo.indexOf(settings.getLocale().getLanguage()));
    }

    private void populateProcessGroup(Composite container) {
        Group processGroup = new Group(container, SWT.SHADOW_IN);
        processGroup.setText(resources.getString("processes"));
        processGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        processGroup.setLayout(new GridLayout(3, false));

        Label updatePeriodLabel = new Label(processGroup, SWT.HORIZONTAL);
        updatePeriodLabel.setText(resources.getString("auto_update_interval"));

        GridData textGd = new GridData(GridData.FILL_HORIZONTAL);
        textGd.horizontalSpan = 2;
        textGd.horizontalIndent = 50;
        updatePeriod = new Spinner(processGroup, SWT.BORDER);
        updatePeriod.setLayoutData(textGd);
        updatePeriod.setMinimum(1);
        updatePeriod.setMaximum(100);
        updatePeriod.setSelection(settings.getUpdatePeriod());

        Label idleShowLabel = new Label(processGroup, SWT.HORIZONTAL);
        idleShowLabel.setText(resources.getString("show_idle_process"));
        GridData labelGd = new GridData(GridData.FILL_HORIZONTAL);
        labelGd.horizontalSpan = 2;
        idleShowLabel.setLayoutData(labelGd);

        showIdleButton = new Button(processGroup, SWT.CHECK);
        showIdleButton.setSelection(settings.getShowIdle());

        Label showBackendPidLabel = new Label(processGroup, SWT.HORIZONTAL);
        showBackendPidLabel.setText(resources.getString("show_pgSqlBlock_process"));
        showBackendPidLabel.setLayoutData(labelGd);
        showBackendPidButton = new Button(processGroup, SWT.CHECK);
        showBackendPidButton.setSelection(settings.getShowBackendPid());
    }

    private void populateNotificationGroup(Composite container) {
        Group notificationGroup = new Group(container, SWT.SHADOW_IN);
        notificationGroup.setText(resources.getString("notifications"));
        notificationGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        notificationGroup.setLayout(new GridLayout(2, false));

        Label idleShowToolTip = new Label(notificationGroup, SWT.HORIZONTAL);
        idleShowToolTip.setText(resources.getString("show_tray_notifications"));
        idleShowToolTip.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        showToolTip = new Button(notificationGroup, SWT.CHECK);
        showToolTip.setSelection(settings.getShowToolTip());

        Label confirmRequiredLabel = new Label(notificationGroup, SWT.HORIZONTAL);
        confirmRequiredLabel.setText(resources.getString("prompt_confirmation_on_process_kill"));
        confirmRequiredLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        confirmRequiredButton = new Button(notificationGroup, SWT.CHECK);
        confirmRequiredButton.setSelection(settings.isConfirmRequired());

        Label confirmExitLabel = new Label(notificationGroup, SWT.HORIZONTAL);
        confirmExitLabel.setText(resources.getString("prompt_confirmation_on_program_close"));
        confirmExitLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        confirmExitButton = new Button(notificationGroup, SWT.CHECK);
        confirmExitButton.setSelection(settings.isConfirmExit());
    }

    private void populateColumnGroup(Composite container) {
        Group columnsGroup = new Group(container, SWT.SHADOW_IN);
        columnsGroup.setText(resources.getString("columns"));
        columnsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        columnsGroup.setLayout(new GridLayout(2, false));

        for (SortColumn column : SortColumn.values()) {
            Button newBtn = new Button(columnsGroup, SWT.CHECK);
            newBtn.setText(column.getName(settings.getResourceBundle()));
            newBtn.setData(column);
            newBtn.setSelection(enabledColumns.contains(column));
            newBtn.setToolTipText(column.toString());
            newBtn.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    Button button = (Button) e.getSource();
                    if (button.getSelection()) {
                        enabledColumns.add((SortColumn)button.getData());
                    } else {
                        enabledColumns.remove((SortColumn)button.getData());
                    }
                }
            });
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(resources.getString("settings"));
    }

    @Override
    protected void okPressed() {
        settings.setUpdatePeriod(updatePeriod.getSelection());
        settings.setShowIdle(showIdleButton.getSelection());
        settings.setShowToolTip(showToolTip.getSelection());
        settings.setShowBackendPid(showBackendPidButton.getSelection());
        settings.setColumnsList(enabledColumns);
        settings.setConfirmRequired(confirmRequiredButton.getSelection());
        settings.setConfirmExit(confirmExitButton.getSelection());
        settings.setLanguage(languageCombo.getText());

        super.okPressed();
    }
}
