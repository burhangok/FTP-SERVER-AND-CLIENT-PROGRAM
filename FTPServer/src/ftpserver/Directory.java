import java.util.ArrayList;


public class Directory {
    private String owner;
    public ArrayList<String> dirListing;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<String> getDirListing() {
        return dirListing;
    }

    public void setDirListing(ArrayList<String> dirListing) {
        this.dirListing = dirListing;
    }

    public Directory() {
           dirListing = new ArrayList<String>();           
    }
}
