package com.safercript.testhttprequest;

import com.safercript.testhttprequest.entity.ImageItem;

import java.util.ArrayList;


public class TestData {


    public static ArrayList<ImageItem> getData() {

        ArrayList<ImageItem> data = new ArrayList<>();

        int[] images = {
                R.drawable.ani_cat_one,
                R.drawable.ani_cat_two,
                R.drawable.ani_cat_three,
                R.drawable.ani_cat_four,
                R.drawable.ani_cat_five,
                R.drawable.ani_cat_six,
                R.drawable.ani_cat_seven,

                R.drawable.ani_dog_one,
                R.drawable.ani_dog_two,
                R.drawable.ani_dog_three,
                R.drawable.ani_dog_four,
                R.drawable.ani_dog_five,

                R.drawable.ani_deer_one,
                R.drawable.ani_deer_two,
                R.drawable.ani_deer_three,
                R.drawable.ani_deer_four,

                R.drawable.bird_parrot_one,
                R.drawable.bird_parrot_two,
                R.drawable.bird_parrot_three,
                R.drawable.bird_parrot_four,
                R.drawable.bird_parrot_five,
                R.drawable.bird_parrot_six,
                R.drawable.bird_parrot_seven,
                R.drawable.bird_parrot_eight
        };

        String[] Categories = {"Cat 1", "Cat 2", "Cat 3", "Cat 4" ,"Cat 5" ,"Cat 6","Cat 7",
                "Dog 1","Dog 2","Dog 3","Dog 4","Dog 5",
                "Deer 1","Deer 2","Deer 3","Deer 4",
                " Parrot 1"," Parrot 2"," Parrot 3"," Parrot 4"," Parrot 5"," Parrot 6"," Parrot 7"," Parrot 8"};

        String[] Weather = {"1 1", "1 2", "1 3", "1 4" ,"1 5" ,"1 6","1 7",
                "2 1","2 2","2 3","2 4","2 5",
                "3 1","3 2","3 3","3 4",
                " 4 1"," 4 2"," 4 3"," 4 4"," 4 5"," 4 6"," 4 7"," 4 8"};

        for (int i = 0; i < images.length; i++) {
            ImageItem current = new ImageItem(images[i],Categories[i],Weather[i]);
            data.add(current);
        }

        return data;
    }

}
