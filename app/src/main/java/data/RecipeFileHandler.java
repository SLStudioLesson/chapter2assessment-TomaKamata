package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RecipeFileHandler {
    private String filePath;

    public RecipeFileHandler() {
        filePath = "app/src/main/resources/recipes.txt";
    }

    public RecipeFileHandler(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 設問1: 一覧表示機能
     * recipes.txtからレシピデータを読み込み、それをリスト形式で返します。 <br> 
     * IOExceptionが発生したときは<i>Error reading file: 例外のメッセージ</i>とコンソールに表示します。
     *
     * @return レシピデータ
     */
    public ArrayList<String> readRecipes() {
        try {
            File dataFile = new File(filePath); // ファイル
            ArrayList<String> recipes = new ArrayList<>(); // レシピ一覧

            // ファイルの有無
            if (dataFile.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(dataFile)); // ファイル読み込み
                String line; //１行読み取り
                while((line = reader.readLine()) != null){
                    // データがあれば1行ごと格納
                    recipes.add(line);
                }
                reader.close();
            }
            return recipes;
        } catch (IOException e) {
            System.out.println("Error reading file:" + e.getMessage());
        }
        return null;
    }

    /**
     * 設問2: 新規登録機能
     * 新しいレシピをrecipes.txtに追加します。<br>
     * レシピ名と材料はカンマ区切りで1行としてファイルに書き込まれます。
     *
     * @param recipeName レシピ名
     * @param ingredients 材料名
     */
     // 
    public void addRecipe(String recipeName, String ingredients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String inputRecipe = recipeName + "," + ingredients; // レシピ名,材料1...
            writer.write(inputRecipe); // 書き込み
            writer.newLine(); // 書き込み後に改行する
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
