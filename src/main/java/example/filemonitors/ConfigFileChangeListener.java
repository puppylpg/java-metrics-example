package example.filemonitors;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;

/**
 * @author liuhaibo on 2018/01/08
 */
public class ConfigFileChangeListener extends FileAlterationListenerAdaptor {
    private FileChange change;

    ConfigFileChangeListener(FileChange fileChange) {
        this.change = fileChange;
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("File created: " + file.getName());
        change.doAction();
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("File changed: " + file.getName());
        change.doAction();
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("File deleted: " + file.getName());
        change.doAction();
    }
}