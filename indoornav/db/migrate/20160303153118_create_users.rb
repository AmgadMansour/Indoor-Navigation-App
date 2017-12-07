class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :first_name
      t.string :last_name
      t.integer :day
      t.integer :month
      t.integer :year
      t.string :gender
      t.boolean :en_route
      t.float :x_coordinate
      t.float :y_coordinate

      t.timestamps null: false
    end
  end
end
