package com.example.joao.crossmotion;

public class FileItem {

    private String FileName;
    private boolean selected;

    public FileItem(String name)
    {
        FileName = name;
        selected = false;
    }
    public boolean getSelected()
    {
        return selected;
    }

    public void setSelected(boolean val)
    {
        selected=val;
    }

    public  String getFileName()
    {
        return FileName;
    }

}
