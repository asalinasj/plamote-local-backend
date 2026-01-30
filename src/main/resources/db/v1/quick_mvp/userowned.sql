
-- select * from user_product_ownership
 -- select * from user_profiles
-- select * from products


select product_id, status, quantity, notes,name, description
from user_profiles
inner join user_product_ownership on user_profiles.id = user_product_ownership.user_id
inner join products on user_product_ownership.product_id = products.id
where user_id = 1

