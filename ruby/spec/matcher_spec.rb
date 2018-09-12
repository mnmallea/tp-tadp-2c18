describe 'Tests para matchers' do

  let :patito do
    pato = Object.new
    pato.send :define_singleton_method, :cuack do
      'cuack cuack'
    end
    pato.send :define_singleton_method, :fly do
      'I believe I can fly'
    end
    pato
  end

  let :an_array do
    an_array = [1,2,3,4]
  end

  it 'duck matchea con cuack y fly' do
    matcher = duck :cuack, :fly
    expect(matcher.call(patito)).to be true
  end

  it 'duck matchea con cuack' do
    matcher = duck :cuack
    expect(matcher.call(patito)).to be true
  end

  it 'duck matchea con ningun mensaje' do
    matcher = duck
    expect(matcher.call(patito)).to be true
  end

  it 'duck no matchea con nadar' do
    matcher = duck :nadar
    expect(matcher.call(patito)).to be false
  end

  it 'un symbol siempre devuelve true' do
    expect(:un_symbol.call('lalal')).to be true
  end

  it 'matchea por valor' do
    expect(val('hola').call('hola')).to be true
  end

  it 'matchea por tipo' do
    expect(type(String).call('un string')).to be true
  end

  it 'debe matchear con la lista' do
    expect(list([1,2,3,4], true).call(an_array)).to be true
  end

  it 'debe matchear si la lista empieza igual' do
    expect(list([1,2,3]).call(an_array)).to be true
  end

  it 'no debe matchear si no es una lista' do
    expect(list([1,2,3]).call('holis')).to be false
  end
end