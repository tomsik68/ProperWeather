package sk.tomsik68.pw.files.api;

import java.io.InputStream;
import java.io.OutputStream;

public interface IDataIO<D extends IData> {

    D load(InputStream in) throws Exception;

    void save(OutputStream out, D data) throws Exception;

}
