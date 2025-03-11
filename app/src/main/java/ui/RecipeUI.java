package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        ArrayList<String> recipes = fileHandler.readRecipes();
        System.out.println();
        if (recipes.size() == 0){
            System.out.println("No recipes available.");
            return;
        }

        System.out.println("Recipes:");
        System.out.println("-----------------------------------"); //
        for(String recipe : recipes){
            String[] recipeDataes = recipe.split(",");
            for (int i = 0; i < recipeDataes.length; i++) {
                // 先頭(recipeDataes[0])はレシピ名
                // ２つ目(recipeDataes[1])以降は材料
                if(i == 0){
                    System.out.println("Recipe Name: " + recipeDataes[i]);
                } else if (i == 1) {
                    System.out.print("Main Ingredients: " + recipeDataes[i]);
                } else {
                    System.out.print("," + recipeDataes[i]);
                }
            }
            System.out.println();
            System.out.println("-----------------------------------");
        }
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        System.out.println();

        // レシピ名入力
        System.out.print("Enter recipe name: ");
        String recipeName = reader.readLine();

        // 材料入力
        System.out.print("Enter main ingredients (comma separated): ");
        String ingredients = reader.readLine();

        // 保存
        fileHandler.addRecipe(recipeName,ingredients);

        // 完了通知
        System.out.println("Recipe added successfully.");
    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        ArrayList<String> recipes = fileHandler.readRecipes(); // レシピ一覧
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'): ");
        String searchText = reader.readLine(); // 検索h内容入力

        System.out.println();
        System.out.println("Search Results:");

        String[] searchWords = searchText.split("&");
        // 項目(name=xxx)ごと
        for (int i = 0; i < searchWords.length; i++) {
            String[] searchWord = searchWords[i].split("=");
            // 属性(name,ingredient)ごと
            if (searchWord.length != 2){
                // 検索方法の不整合により検索結果なし
                System.out.println("No recipes found matching the criteria.");
                return;
            } else if (searchWord[0].equals("name")){
                // 該当行以外を削除 (行ごと)
                for (int j = recipes.size() - 1; j >= 0; j--) {
                    // レシピの内、検索に引っかからないものを削除
                    String[] recipeDataes = recipes.get(j).split(",");
                    // 先頭(recipeDataes[0])はレシピ名
                    if(recipeDataes[0].indexOf(searchWord[1]) == -1){
                        recipes.remove(j); // 削除
                    }
                }
            } else if (searchWord[0].equals("ingredient")){
                // 該当行以外を削除 (行ごと)
                for (int j = recipes.size() - 1; j >= 0; j--) {
                    // レシピの内、検索に引っかからないものを削除
                    String[] recipeDataes = recipes.get(j).split(",");
                    boolean search = false; // 該当するものがあったかどうか
                    for (int k = 0; k < recipeDataes.length; k++) {
                        // ２つ目(recipeDataes[1])以降は材料
                        if (k != 0 && recipeDataes[k].indexOf(searchWord[1]) != -1) {
                            search = true;
                        }
                    }
                    if (search == false){
                        recipes.remove(j); // 削除
                    }
                }
            } else {
                // 検索方法の不整合により検索結果なし
                System.out.println("No recipes found matching the criteria.");
                return;
            }
        }
        // 検索結果
        // ない場合は「No recipes found matching the criteria.」と出力し終了
        if (recipes.size() == 0){
            System.out.println("No recipes found matching the criteria.");
            return;
        }
        for(String recipe : recipes) {
            System.out.println(recipe);
        }
    }
}

