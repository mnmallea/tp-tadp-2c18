describe Prueba do

  x = [1, 2, 3]

  it 'caso facil' do
    matches?(x) do
      otherwise {puts x}
    end
  end

  it 'should' do
    matches?(x) do
      with(list([:a, val(2), duck(:+)])) {a + 2}
      with(list([1, 2, 3])) {'acá no llego'}
      otherwise {'acá no llego'}
    end
  end

end