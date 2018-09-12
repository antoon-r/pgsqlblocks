package newview;

import org.eclipse.swt.graphics.Image;
import ru.taximaxim.treeviewer.models.IColumn;
import ru.taximaxim.treeviewer.models.MyTreeViewerDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 16.08.18.
 */
public class ExampleDataSource extends MyTreeViewerDataSource{

    protected final ResourceBundle resourceBundle;

    public ExampleDataSource(ResourceBundle resourceBundle) {
        super();
        this.resourceBundle = resourceBundle;
    }

    @Override
    public boolean columnIsSortable() {
        return true;
    }

    @Override
    public List<IColumn> getColumns() {
        List<IColumn> list = Arrays.asList(Columns.values());
        return list;
    }

    @Override
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    @Override
    public String getRowText(Object element, IColumn iColumn) {
        Test test = (Test) element;
        switch (iColumn.getColumnName()){
            case "title":
                return test.getTitle();
            case "name":
                return test.getName();
            case "author":
                return test.getAuthor();
            case "price":
                return String.valueOf(test.getPrice());
        }
        return "TEXT";
    }

    @Override
    public Object[] getElements(Object inputElement) {
        List<Test> list = (List<Test>) inputElement;
        return list.toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        Test testObject = (Test)parentElement;
        return testObject.getChildren().toArray();
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        Test testObject = (Test) element;
        return testObject.hasChildren();
    }
}
