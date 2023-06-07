-- noinspection SqlWithoutWhereForFile
-- noinspection SqlResolveForFile
DELETE
FROM country_restaurants;
DELETE
FROM recipe_urls;
DELETE
FROM recipe_restaurants;
DELETE
FROM gastronomic_culture_recipes;
DELETE
FROM gastronomic_culture_countries;
DELETE
FROM gastronomic_category_products;
DELETE
FROM gastronomic_culture_categories;
DELETE
FROM dish_multimedia;
DELETE
FROM recipe;
DELETE
FROM michelin_star;
DELETE
FROM representative_product;
DELETE
FROM gastronomic_category;
DELETE
FROM restaurant;
DELETE
FROM country;
DELETE
FROM gastronomic_culture;


----------Gastronomic Culture---------
INSERT INTO gastronomic_culture (name, url, description)
VALUES ('Italian Cuisine',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/0/03/Flag_of_Italy.svg/1200px-Flag_of_Italy.svg.png',
        'Traditional Italian dishes and flavors.'),
       ('Mexican Cuisine', 'https://m.media-amazon.com/images/I/61YBZx+l3bL._AC_SL1500_.jpg',
        'Spicy and flavorful Mexican dishes.'),
       ('Japanese Cuisine',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Flag_of_Japan.svg/640px-Flag_of_Japan.svg.png',
        'Delicate and fresh Japanese dishes.'),
       ('Indian Cuisine',
        'https://media.istockphoto.com/id/460844989/es/foto/bandera-india.jpg?s=612x612&w=0&k=20&c=bWMlPFGeQc3c1JxpPAdZfyp2j9DFRAcIqJojLGmguik=',
        'Rich and aromatic Indian dishes.'),
       ('Colombian Cuisine',
        'https://i0.wp.com/casatextil.co/wp-content/uploads/2018/05/Tela-bandera-de-Colombia-en-antifluido-impermeable.jpg?fit=700%2C500&ssl=1',
        'Rich and tropical Colombian dishes.');

--------- Country ----------------
INSERT INTO country (name, iso)
VALUES ('Italy', 'IT'),
       ('Mexico', 'MX'),
       ('Japan', 'JA'),
       ('India', 'IN'),
       ('Colombia', 'CO');

----------Restaurant----------------
INSERT INTO restaurant (name, city, contact, country_id)
VALUES ('La taqueria', 'Monterey', 'info@lataqueria.com', 2),
       ('La Pizzeria', 'Rome', 'info@pizzeria.com', 1),
       ('Sushi Paradise', 'Tokyo', 'info@sushiparadise.com', 3),
       ('Curry House', 'Mumbai', 'info@curryhouse.com', 4),
       ('Andres', 'Bogota', 'info@andres.com', 5);

----------Gastronomic Category---------
INSERT INTO gastronomic_category (name, culture_id)
VALUES ('Appetizers', 1),
       ('Main Courses', 2),
       ('Desserts', 3),
       ('Street Food', 4),
       ('Beverages', 5);

-----------Representative Product---------
INSERT INTO representative_product (name, brand, category_id)
VALUES ('Cheese', 'Kraft', 1),
       ('Olive Oil', 'Bertolli', 2),
       ('hot dog', 'zenu', 4),
       ('Coffee', 'Starbucks', 5),
       ('Chocolate', 'Lindt', 3);

------------Michelin Star-----------
INSERT INTO michelin_star (acquired, restaurant_id)
VALUES ('2022-01-15', 1),
       ('2021-08-27', 2),
       ('2023-02-10', 3),
       ('2022-11-05', 4),
       ('2023-04-18', 5);

----------Recipe---------
INSERT INTO recipe (name, description, instructions, culture_id)
VALUES ('Tacos al Pastor', 'Delicious Mexican dish',
        'In a large bowl, combine the achiote paste, white vinegar, vegetable oil, dried oregano, ground cumin, smoked paprika, salt, black pepper, and minced garlic. Mix well to create a marinade. Add the sliced pork shoulder to the marinade and toss until the meat is well coated. Cover the bowl and refrigerate for at least 2 hours, or preferably overnight, to allow the flavors to develop. Preheat your grill or grill pan over medium-high heat. If using a grill pan, lightly grease it with vegetable oil to prevent sticking. Thread the marinated pork slices onto skewers, allowing some space between each piece. Grill the pork skewers for about 10-12 minutes, turning occasionally, until the meat is cooked through and slightly charred. Baste the pork with pineapple juice during grilling to add flavor and moisture. Once cooked, remove the pork from the skewers and transfer to a cutting board. Allow it to rest for a few minutes, then thinly slice it. Warm the corn tortillas on a griddle or in a dry skillet over medium heat. To serve, place some sliced pork onto each warmed tortilla. Top with chopped onion, cilantro, and sliced pineapple, if desired. Squeeze fresh lime juice over the tacos for added brightness. Enjoy your delicious homemade tacos al pastor!',
        1),
       ('Pasta Carbonara', 'Classic Italian pasta dish',
        'Bring a large pot of salted water to a boil. Cook the spaghetti or fettuccine according to the package instructions until al dente. Drain, reserving 1/2 cup of the pasta cooking water. While the pasta is cooking, heat the olive oil in a large skillet over medium heat. Add the diced pancetta or bacon and cook until crispy and browned, about 5-7 minutes. Add the minced garlic and cook for an additional 1 minute. In a medium bowl, whisk together the eggs, grated Parmesan cheese, and grated Pecorino Romano cheese. Season with salt and black pepper. Remove the skillet from the heat and let it cool slightly. Add the drained pasta to the skillet and toss it with the pancetta or bacon and garlic, coating the pasta evenly. Pour the egg and cheese mixture over the pasta, tossing quickly to coat the strands. The heat from the pasta will cook the eggs and create a creamy sauce. If the sauce seems too thick, gradually add the reserved pasta cooking water, a little at a time, until desired consistency is reached. Season with additional salt and black pepper to taste. Garnish with chopped parsley, if desired. Serve the pasta carbonara immediately while its still hot. 	Enjoy your delicious homemade pasta carbonara!',
        2),
       ('Sushi Rolls', 'Traditional Japanese sushi',
        'Rinse the sushi rice in cold water until the water runs clear. Drain the rice and cook it according to the package instructions. Once the rice is cooked, transfer it to a large bowl and let it cool slightly. While its still warm, season the rice with sushi vinegar (a mixture of rice vinegar, sugar, and salt) and gently mix it in with a wooden spoon or spatula. Allow the rice to cool completely.Prepare your desired fillings by cutting them into long, thin strips. Common options include cucumber, avocado, crab sticks, smoked salmon, and tuna. You can mix and match fillings to create different combinations. Place a bamboo sushi mat on a clean surface and lay a sheet of nori on top of it. Wet your hands with water to prevent sticking, and take a handful of sushi rice. Spread the rice evenly over the nori, leaving about an inch of space at the top. Arrange your chosen fillings in a line across the center of the rice. Using the sushi mat, roll the nori tightly over the fillings, using gentle pressure to shape the roll. Wet the top edge of the nori with water to seal it. Repeat the process with the remaining nori sheets and fillings. Once all the rolls are assembled, use a sharp knife to slice each roll into bite-sized pieces. Dip the knife in water between each cut to prevent sticking. Serve the sushi rolls with soy sauce for dipping, along with wasabi and pickled ginger on the side for extra flavor.Enjoy your homemade sushi rolls!',
        3),
       ('Chicken Curry', 'Spicy Indian curry dish',
        'Heat the vegetable oil in a large pan or skillet over medium heat. Add the chopped onion and cook until softened and lightly browned, about 5 minutes. Add the minced garlic and grated ginger to the pan, and cook for another 1-2 minutes until fragrant. In a small bowl, mix together the curry powder, ground cumin, ground coriander, turmeric, and cayenne pepper (if using). Add the spice mixture to the pan and stir well to coat the onions, garlic, and ginger. Add the chicken pieces to the pan and cook until they are browned on all sides, about 5-7 minutes. Pour in the coconut milk, chicken broth, tomato paste, soy sauce, and brown sugar. Stir to combine all the ingredients. Bring the mixture to a simmer, then reduce the heat to low and cover the pan. Let the curry simmer for 20-25 minutes, or until the chicken is cooked through and tender. Taste the curry and season with salt according to your preference. Serve the chicken curry over cooked rice or with naan bread. Garnish with fresh cilantro. Enjoy your flavorful homemade chicken curry!',
        4),
       ('Bandeja Paisa', 'Huge Colombian mix of flavours',
        'Cook the white rice and red beans separately according to their package instructions. Set aside. In a large skillet, cook the bacon until crispy. Remove the bacon from the skillet and set aside. In the same skillet, add the ground beef and pork sausage. Cook until browned and fully cooked. Remove from heat and set aside. In a separate skillet, heat vegetable oil over medium heat. Fry the plantain slices until golden brown and tender. Remove from the skillet and drain on paper towels. In the same skillet, fry the eggs to your desired doneness (typically sunny-side-up or over-easy). Assemble the bandeja paisa: On a large serving platter, arrange the cooked rice, red beans, cooked bacon, ground beef, pork sausage, fried plantains, fried eggs, avocado slices, cooked white corn kernels, and shredded beef or pork (if using). Pour the hogao sauce over the rice and beans. Garnish with chopped fresh cilantro. Season the entire platter with salt and black pepper to taste. Serve the bandeja paisa immediately while everything is still warm. Enjoy this hearty and traditional Colombian dish!',
        5);

----------- Dish Multimedia ------------
INSERT INTO dish_multimedia (url, recipe_id)
VALUES ('https://www.seriouseats.com/thmb/4kbwN13BlZnZ3EywrtG2AzCKuYs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/20210712-tacos-al-pastor-melissa-hom-seriouseats-37-f72cdd02c9574bceb1eef1c8a23b76ed.jpg',
        1),
       ('https://chefcompass.com/wp-content/uploads/pastor_tacos.jpg', 1),
       ('https://www.thespruceeats.com/thmb/ovIQQQxQajADuIE2lqhgqq7ppyE=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/pasta-carbonara-recipe-5210168-hero-01-80090e56abc04ca19d88ebf7fad1d157.jpg',
        2),
       ('https://ichisushi.com/wp-content/uploads/2022/05/Best-Hawaiian-Roll-Sushi-Recipes.jpg', 3),
       ('https:https://www.allrecipes.com/thmb/FL-xnyAllLyHcKdkjUZkotVlHR8=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/46822-indian-chicken-curry-ii-DDMFS-4x3-39160aaa95674ee395b9d4609e3b0988.jpg',
        4),
       ('https://cdn.colombia.com/gastronomia/2011/08/02/bandeja-paisa-1616.gif', 5),
       ('https://i0.wp.com/hatoviejo.com/prueba/wp-content/uploads/2021/08/20210616_BandejaPaisa_018_1000.jpg?fit=1000%2C666&ssl=1',
        5);

---------Gastronomic Culture Categories---------
INSERT INTO gastronomic_culture_categories (categories_id, gastronomic_culture_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

----------Gastronomic Category Product---------
INSERT INTO gastronomic_category_products (gastronomic_category_id, products_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

----------Gastronomic Culture Country----------
INSERT INTO gastronomic_culture_countries (countries_id, cultures_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

----------Gastronomic Culture Recipies----------
INSERT INTO gastronomic_culture_recipes (gastronomic_culture_id, recipes_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

----------Recipe Restaurant--------
INSERT INTO recipe_restaurants (recipes_id, restaurants_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

-----------Recipe urls------
INSERT INTO recipe_urls (recipe_id, urls_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

---------- Country_Restaurants ---------
INSERT INTO country_restaurants (country_id, restaurants_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);


-- Para cuando quiera saber todos los nombres de las tablas
-- SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC';
-- Para cuando quiera reiniciar las secuencias
-- SELECT 'ALTER SEQUENCE ' || SEQUENCE_NAME || ' RESTART WITH 1;' FROM INFORMATION_SCHEMA.SEQUENCES;