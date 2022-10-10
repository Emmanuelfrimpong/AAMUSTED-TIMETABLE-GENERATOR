/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

/**
 *
 * @author emman
 */
public class TableRowItem {
    String col1,col2,col3,col4;

    public TableRowItem(String col1, String col2, String col3, String col4) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
    }

    public TableRowItem() {
    }
    
    

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }
    
    public String getValAt(int i){
        switch (i) {
            case 0:
                return this.col1;
            case 1:
                return this.col2;
            case 2:
                return this.col3;
            case 3:
                return this.col4;
            default:
                return "";
        }
    }
    
    
    
}
