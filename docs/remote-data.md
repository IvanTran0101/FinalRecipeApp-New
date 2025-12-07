# Remote recipes JSON fetch guide

To make sure the sample recipes JSON can be fetched by the app, check these items:

1. **Push `app/src/main/recipes.json` to GitHub** on the `main` branch so the raw URL exists.
2. **Verify the raw URL is reachable** in a browser or with curl:
   ```sh
   curl -I https://raw.githubusercontent.com/IvanTran0101/FinalRecipeApp-New/main/app/src/main/recipes.json
   ```
   A `200` response means the app can download the file.
3. The app already uses `https` and has `INTERNET` permission in `AndroidManifest.xml`, so no extra client changes are needed.
4. If you update the JSON, push the changes so the raw URL always reflects the latest data.

With these steps completed, the app's `RecipeApiService` will fetch the sample recipes successfully.

## What to do on the `main` branch

Follow these commands on the `main` branch to publish the JSON so the app can fetch it:

1. Check out `main` and pull the latest changes:
   ```sh
   git checkout main
   git pull origin main
   ```
2. Merge your working branch that contains `app/src/main/recipes.json` and the URL fix:
   ```sh
   git merge work
   ```
   Resolve any conflicts if prompted.
3. Push to GitHub so the raw URL becomes available:
   ```sh
   git push origin main
   ```
4. Re-run the curl check to confirm the raw file returns `200` before testing the app.
