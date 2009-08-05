#keytool -genkey -keystore jac.keys -alias OT
#keytool -import -alias JAC -file /ftp/pub/JAC_CA.crt -keystore jac.keys
#jarsigner -keystore jac.keys -signedjar cfg.jar  lib/cfg.jar OT

print "Enter password for keystore : " ;
$pass = <STDIN> ;
chomp( $pass ) ;

chomp( $cwd = `pwd` ) ;
unless( -e "$cwd/work/jac.keys" ){ &genkey( $pass ) ; }
if( -e "$cwd/work/saved" ){ print `rm -f $cwd/work/saved/*` ; }
else{ print `mkdir $cwd/work/saved` ; }

@dirs = ( 'lib' , 'tools' ) ;
foreach $dir ( @dirs )
{
	chdir( $dir ) ;
	@files = `find . -name "*.jar"` ;
	foreach $file ( @files )
	{
		chomp( $file ) ;
		@path = split( '' , $file ) ;
		shift @path ; shift @path ;
		$file = join( '' , @path ) ;
		$output = `jarsigner -verify $file` ;
		if( $output =~ "missing" )
		{
			print "Signing $file ... \n" ;
			system( 'mv' , ( $file , "$cwd/work/saved/$file" ) ) ;
			system( 'jarsigner' , 
				( 
					'-keystore' , "$cwd/work/jac.keys" 
					, '-storepass' , $pass  
					, '-signedjar' , "$cwd/$dir/$file" 
					, "$cwd/work/saved/$file" , 'OT'
				) 
				) ;
		}
	}
	chdir( $cwd ) ;
}

sub genkey
{
	$password = shift or die "No password provided for keygen\n" ;
	system
	( 'keytool' ,
		(
			'-genkey' ,
			'-keystore' , "$cwd/work/jac.keys" ,
			'-alias' , 'OT' ,
			'-storepass' , $password
		)
	) ;

	system
	( 'keytool' ,
		(
			'-import' ,
			'-alias' , 'JAC' ,
			'-file' , '/ftp/pub/JAC_CA.crt' ,
			'-keystore' , "$cwd/work/jac.keys" ,
			'-storepass' , $password
		)
	) ;
}
