class Destination < ActiveRecord::Base
  # Fields

  # Validations
  validates :name, :description, :x_coordinate, :y_coordinate, :map_id, :category_id, presence: true

  # Relations
  has_one :map
  has_many :categories

  # Callbacks

  # Class Methods

  # Methods

end
