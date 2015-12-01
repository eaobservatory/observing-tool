# keytool -genkey -keystore jac.keys -alias OT
# keytool -import -alias EAO -file /ftp/pub/EAO_CA.crt -keystore jac.keys
# jarsigner -keystore jac.keys -signedjar cfg.jar  lib/cfg.jar OT

print "Enter password for keystore : ";
$pass = <STDIN>;
chomp( $pass );

@paths = split('/' , $0);
pop(@paths);
$self = join('/' , @paths);
if ($self ne "") {
    $self .= '/';
}

chomp ($cwd = `pwd`);

$keystore = $cwd . "/" . $self . "jac.keys";
$saveddir = $cwd . "/" . $self . "saved";

unless (-e $keystore) {
    &genkey($pass);
}

if (-e $saveddir) {
    print `rm -f $saveddir/*`;
}
else {
    print `mkdir $saveddir`;
}

@dirs = ('lib');
foreach $dir (@dirs) {
    chdir( $dir ) or next;
    @files = `find . -name "*.jar"`;

    foreach $file (@files) {
        chomp( $file );
        @path = split('' , $file);
        shift @path;
        shift @path;
        $file = join( '' , @path );

        $output = `jarsigner -verify $file`;

        if ($output =~ "missing") {
                print "Signing $file ... \n";
                system('mv', ($file, $file . "-unsigned"));
                system('jarsigner', (
                        '-keystore', $keystore,
                        '-storepass', $pass,
                        '-signedjar', "$cwd/$dir/$file",
                        "$cwd/$dir/" . $file . "-unsigned", 'OT',
                ));
                system('mv', ("$cwd/$dir/" . $file . "-unsigned", $saveddir));
        }
    }
    chdir( $cwd );
}

sub genkey {
    $password = shift or die "No password provided for keygen\n";
    system('keytool', (
            '-genkey',
            '-keystore', $keystore,
            '-alias', 'OT',
            '-storepass', $password,
    ));

    system('keytool', (
            '-import',
            '-alias', 'EAO',
            '-file', '/ftp/pub/EAO_CA.crt',
            '-keystore', $keystore,
            '-storepass', $password,
    ));
}
