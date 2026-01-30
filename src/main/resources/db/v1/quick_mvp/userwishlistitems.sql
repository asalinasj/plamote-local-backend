select wishlists.id as wishlist_id ,product_id, products.name as product_name, target_price, notes
from user_profiles
inner join wishlist_items on user_profiles.id = wishlist_items.wishlist_id
inner join wishlists on wishlists.user_id = wishlist_items.wishlist_id
inner join products on wishlist_items.product_id = products.id
where user_profiles.id = 1 AND wishlists.id = 1
order by wishlists.id