class Category < ActiveRecord::Base
  # Fields

  # Validations
  validates :name, presence: true

  # Relations
  has_many :destinations
  has_one :map

  # Callbacks

  # Class Methods

  # Methods
end
