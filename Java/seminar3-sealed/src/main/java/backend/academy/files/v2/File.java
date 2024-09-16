package backend.academy.files.v2;

// V2
public class File {
    private String name;
    private String path;
    /* ... */
    public boolean exists()                                 { return true; }
    public byte[] read()                                    { return new byte[0]; }
    public void write(byte[] data)                          { }
    public boolean create()                                 { return true; }
}
