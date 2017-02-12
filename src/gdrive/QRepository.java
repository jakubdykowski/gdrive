package gdrive;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class QRepository
{
  private final String separator;
  private final URL repository;
  private final String[][] rep;
  private final String[] files;
  private final String[] names;
  private final String[] description;
  private final String[] title;
  private final String[] version;
  private final String[] included;

  public QRepository(URL http)
    throws IOException
  {
    this.repository = http;
    Object[] repo = null;
    repo = XML.read(http);
    this.separator = "\\|";
    this.rep = new String[10][repo.length];
    this.files = new String[repo.length];
    this.names = new String[repo.length];
    this.title = new String[repo.length];
    this.description = new String[repo.length];
    this.version = new String[repo.length];
    this.included = new String[repo.length];
    this.rep[0] = this.names;
    this.rep[1] = this.files;
    this.rep[2] = this.description;
    this.rep[3] = this.title;
    this.rep[4] = this.version;
    this.rep[5] = this.included;

    for (int i = 0; i < repo.length; i++) {
      String[] arg = ((String)repo[i]).split(this.separator);
      for (int n = 0; n < arg.length; n++)
        this.rep[n][i] = arg[n];
    }
  }

  public String[] search(String filename)
  {
    ArrayList a = new ArrayList();
    for (int i = 0; i < this.names.length; i++) { if (!this.names[i].contains(filename)) continue; a.add(this.names[i]); }
    String[] results = new String[a.size()];
    if (a.isEmpty()) return null;
    for (int i = 0; i < a.size(); i++) results[i] = ((String)a.get(i));
    return results;
  }
  public String getAdress(String filename) {
    for (int i = 0; i < this.names.length; i++) {
      if (this.names[i].equals(filename)) return this.files[i];
    }
    return null;
  }
  public int getID(String filename) {
    for (int i = 0; i < this.names.length; i++) {
      if (this.names[i].equals(filename)) return i;
    }
    return -1;
  }
  public Repo[] getAll() {
    try {
      Repo[] r = new Repo[this.names.length];
      for (int i = 0; i < this.names.length; i++) {
        r[i] = new Repo(i, this.rep);
//        r[i] = new Repo(i, this.rep, null);
      }
      return r; } catch (Exception e) {
    }return null;
  }

  public Repo get(String filename) {
    for (int i = 0; i < this.names.length; i++) {
      if (this.names[i].equals(filename)) return new Repo(i, this.rep);
    }
    return null;
  }
  public Repo get(int id) {
    try {
      return new Repo(id, this.rep); } catch (Exception e) {
    }return null;
  }

  public boolean equal(String filename) {
    for (int i = 0; i < this.names.length; i++) {
      if (this.names[i].equals(filename)) return true;
    }
    return false;
  }
  public int getSize() {
    if (this.names.length != 0) return this.names.length;
    return 0; }
  public class Repo { public final String NAME;
    public final String ADRESS;
    public final String DESCRIPTION;
    public final String TITLE;

    private Repo(int index, String[][] reps) { this.NAME = reps[0][index];
      this.ADRESS = reps[1][index];
      this.DESCRIPTION = reps[2][index];
      this.TITLE = reps[3][index];
    }
  }
}