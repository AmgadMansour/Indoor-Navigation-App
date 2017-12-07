class CreateRooms < ActiveRecord::Migration
  def change
    create_table :rooms do |t|
      t.string :name
      t.float :xmin
      t.float :ymin
      t.float :xmax
      t.float :ymax

      t.timestamps null: false
    end
  end
end
