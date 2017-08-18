package scutum.scanner.endpointprotection.contracts;

public class Process {
    private int id;
    private int parentId;
    private String owner;
    private String path;
    private String hash;
    private long size;

    public Process(int id,
                   int parentId,
                   String owner,
                   String path,
                   String hash,
                   long size) {

        this.setId(id);
        this.setParentId(parentId);
        this.setOwner(owner);
        this.setPath(path);
        this.setHash(hash);
        this.setSize(size);
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getOwner() {
        return owner;
    }

    public String getPath() {
        return path;
    }

    public String getHash() {
        return hash;
    }

    public long getSize() {
        return size;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
