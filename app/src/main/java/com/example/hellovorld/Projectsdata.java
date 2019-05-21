package com.example.hellovorld;

public class Projectsdata {

    private String Name;
   // private String nocompleted;
 //   private String total;

    public Projectsdata() {
        // needed for binding
    }

    public Projectsdata(String name) {
        Name = name;
    //    this.nocompleted= nocompleted;
    //    this.total = total;
    }

    public String getName() {
        return Name;
    }

  //  public String getnocompleted() {
  //      return nocompleted ;
  //  }

 //   public void setnocompleted(String nocompleted) {
//        this.nocompleted = nocompleted;
 //   }

 //   public String getTotal() {
//        return total;
//    }

   // public void setTotal(String total) {
   //     this.total = total;
 //   }

    public void setName(String name) {
        Name = name;
    }
}
