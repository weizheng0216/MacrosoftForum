# Deploy our web front-end localy, which will let us test our code faster and safer.

# Local deploy folder
TARGETFOLDER=./local

# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder tree, and then make it from scratch
rm -rf $TARGETFOLDER
mkdir $TARGETFOLDER

# html pages
cp index.html $TARGETFOLDER
cp login.html $TARGETFOLDER

# step 2: update our npm dependencies
# npm update     # comment out to save time

# step 3: copy jQuery, Handlebars, and Bootstrap files
cp node_modules/jquery/dist/jquery.min.js $TARGETFOLDER
cp node_modules/handlebars/dist/handlebars.min.js $TARGETFOLDER
cp node_modules/bootstrap/dist/js/bootstrap.min.js $TARGETFOLDER
cp node_modules/bootstrap/dist/css/bootstrap.min.css $TARGETFOLDER
cp -R node_modules/bootstrap/dist/fonts $TARGETFOLDER

# step 4: compile TypeScript files. Errors are suppressed
node_modules/typescript/bin/tsc app.ts --strict --outFile $TARGETFOLDER/app.js 2>&1 1>/dev/null

# step 5: copy css files
cat css/*.css> $TARGETFOLDER/app.css

# step 6: compile handlebars templates to the deploy folder
for hbr in $(ls hbr/); do
    node_modules/handlebars/bin/handlebars hbr/$hbr >> $TARGETFOLDER/templates.js
done

# step 7: set up Jasmine
node_modules/typescript/bin/tsc apptest.ts --strict --outFile $TARGETFOLDER/apptest.js 2>&1 1>/dev/null
cp spec_runner.html $TARGETFOLDER
cp node_modules/jasmine-core/lib/jasmine-core/jasmine.css $TARGETFOLDER
cp node_modules/jasmine-core/lib/jasmine-core/jasmine.js $TARGETFOLDER
cp node_modules/jasmine-core/lib/jasmine-core/boot.js $TARGETFOLDER
cp node_modules/jasmine-core/lib/jasmine-core/jasmine-html.js $TARGETFOLDER
