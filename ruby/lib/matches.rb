module Matches
  def matches?(an_object, &block)
    an_object.instance_exec &block
  end
end