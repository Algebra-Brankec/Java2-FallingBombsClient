/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author brand
 */
public final class ObjectUtil {
    public static byte[] objectToByteArray(Object obj) throws IOException {
        byte[] objBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            objBytes = baos.toByteArray();
            
            return objBytes;
        }
    }
    
    public static Object byteArrayToObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        ArrayList<Object> objectList;
        try {
            @SuppressWarnings("unchecked")
            ArrayList<Object> list = (ArrayList<Object>) ois.readObject();
            objectList = list;
        } finally {
            ois.close();
        }
        
        return objectList;
    }
}
