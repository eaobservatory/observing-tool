@jars = `find . -name "*.jar"` ;
@signed = () ;
@unsigned = () ;
$count = 0 ;
foreach $jar ( @jars )
{
	print "\r" . $count++ . " of " . $#jars ;
	chomp( $jar ) ;
	$output = `jarsigner -verify $jar` ;
	if( $output =~ "missing" )
	{
		push( @unsigned , $jar ) ;
	}
	else
	{
		push( @signed , $jar ) ;
	}
}
print "\n" . "-" x 20 . "\n" ;
print "Unsigned jars\n" ;
print "-" x 20 . "\n" ;
foreach $entry ( @unsigned )
{
	print $entry . "\n" ;
}
print "-" x 20 . "\n" ;
print "Signed jars\n" ;
foreach $entry ( @signed )
{
	print "-" x 20 . "\n" ;
        print $entry . "\n" ;
	@verified = `jarsigner -verify -verbose -certs $entry` ;
	foreach $line ( @verified )
	{
		chomp( $line ) ;
		$line =~ /\s+X.509, CN=(.+),/ ;
		if( defined $1 ){ $signer = $1 ; }
	}
	print $signer . "\n" ;
}
print "-" x 20 . "\n" ;
