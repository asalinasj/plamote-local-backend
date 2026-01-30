-- select * from wishlist_items;

-- select * from user_profiles;

-- select * from wishlists

-- select * from user_profiles join wishlist_items on user_profiles.id = wishlist_items.wishlist_id;

select DISTINCT name, description, is_public, wishlists.created_at, wishlists.updated_at
from user_profiles
inner join wishlist_items on user_profiles.id = wishlist_items.wishlist_id
inner join wishlists on wishlists.user_id = wishlist_items.wishlist_id
where user_profiles.id = 1




