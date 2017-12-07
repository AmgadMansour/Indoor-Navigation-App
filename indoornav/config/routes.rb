Rails.application.routes.draw do
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # API
  namespace :api, defaults: { format: :json } do
    resources :users, only: [:show, :create] do
      member do
        get :user_destinations
        post :update_location
      end
    end

    #patch '/users/:id/update_location/:x_coordinate/:y_coordinate' => 'users#update_location', :as => 'update_location'

    resources :destinations, only: :show

    get '/destinations/map/:map_id' => 'destinations#all_destinations', :as => 'all_destinations'
    get '/destinations/map/:map_id/:category' => 'destinations#destinations_by_category', :as => 'destinations_by_category'

    get '/transitions/:map_id' => 'transitions#all_transitions', :as => 'all_transitions'

    get '/beacons/:map_id' => 'beacons#all_beacons', :as => 'all_beacons'

    get '/graphs/:map_id' => 'graphs#show', :as => 'graph_show'

    get '/categories/:map_id' => 'categories#all_categories', :as => 'all_categories'

    get '/rooms/:map_id' => 'rooms#all_rooms', :as => 'all_rooms'

    resources :maps, only: :show

    resources :sessions, only: :create
  end

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
