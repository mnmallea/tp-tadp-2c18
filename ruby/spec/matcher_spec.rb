require 'rspec'
require_relative '../lib/matcher'
require_relative '../lib/metamodelo'

describe 'Tests para matchers' do

  let :duck do
    pato = Object.new
    pato.send :define_singleton_method, :cuack do
      'cuack cuack'
    end
    pato.send :define_singleton_method, :fly do
      'I believe I can fly'
    end
    pato
  end

  it 'duck matchea con cuack y fly' do
    matcher = Matcher.duck :cuack, :fly
    expect(matcher.call(duck)).to be true
  end

  it 'duck matchea con cuack' do
    matcher = Matcher.duck :cuack
    expect(matcher.call(duck)).to be true
  end

  it 'duck matchea con ningun mensaje' do
    matcher = Matcher.duck
    expect(matcher.call(duck)).to be true
  end

  it 'duck no matchea con nadar' do
    matcher = Matcher.duck :nadar
    expect(matcher.call(duck)).to be false
  end

  it 'un symbol siempre devuelve true' do
    expect(:un_symbol.call('lalal')).to be true
  end

  it 'matchea por valor' do
    expect(Matcher.val('hola').call('hola')).to be true
  end

  it 'matchea por tipo' do
    expect(Matcher.type(String).call('un string')).to be true
  end
end