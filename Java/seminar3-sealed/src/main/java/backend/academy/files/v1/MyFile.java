package backend.academy.files.v1;

class MyFile extends File {
    public void create() {
        if (System.getProperty("os.name").equals("Windows")) {
            // Implementation of creation
        } else {
            throw new UnsupportedOperationException("OS is not Windows");
        }
    }
}
