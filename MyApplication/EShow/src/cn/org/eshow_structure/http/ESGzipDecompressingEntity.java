package cn.org.eshow_structure.http;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
/**
 * Http解压内容
 *
 */
public class ESGzipDecompressingEntity extends HttpEntityWrapper{
    
    public ESGzipDecompressingEntity(final HttpEntity entity){
        super(entity);
    }

    public InputStream getContent() throws IOException, IllegalStateException{
        InputStream wrappedin = wrappedEntity.getContent();
        return new GZIPInputStream(wrappedin);
    }

    public long getContentLength(){
        return -1;
    }
}